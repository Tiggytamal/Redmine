/*******************************************************************************
 * Licensed Materials - Property of IBM
 * FindChangedFiles.java
 * (c) Copyright IBM Corporation 2012.
 * U.S. Government Users Restricted Rights: Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 *******************************************************************************/

package control.rtc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ibm.team.filesystem.common.workitems.ILinkConstants;
import com.ibm.team.links.client.ILinkManager;
import com.ibm.team.links.common.IItemReference;
import com.ibm.team.links.common.ILink;
import com.ibm.team.links.common.ILinkCollection;
import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ILoginHandler2;
import com.ibm.team.repository.client.ILoginInfo2;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.client.login.UsernameAndPasswordLoginInfo;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.scm.client.IWorkspaceManager;
import com.ibm.team.scm.common.IChange;
import com.ibm.team.scm.common.IChangeSet;
import com.ibm.team.scm.common.IChangeSetHandle;
import com.ibm.team.scm.common.IVersionable;
import com.ibm.team.scm.common.IVersionableHandle;
import com.ibm.team.workitem.client.IWorkItemClient;
import com.ibm.team.workitem.common.model.IWorkItem;

/**
 * <p>
 * Sometimes it is helpful to get a list of changed files and folders on a work items without having
 * to manually examine each attached change set. The example program takes a work item ID and prints
 * out this information to standard out.
 * </p>
 */
public class FindChangedFiles
{
    public static void main(final String[] args) throws TeamRepositoryException
    {
        if (args.length < 4)
        {
            System.out.println("Please provide repository URI, username, password and work item ID as first four arguments to this program.");
            System.exit(1);
        }

        String repoUri = args[0];
        final String userId = args[1];
        final String password = args[2];

        TeamPlatform.startup();

        try
        {
            // Login to the repository using the provided credentials
            ITeamRepository repo = TeamPlatform.getTeamRepositoryService().getTeamRepository(repoUri);
            repo.registerLoginHandler(new ILoginHandler2() {
                @Override
                public ILoginInfo2 challenge(ITeamRepository arg0)
                {
                    return new UsernameAndPasswordLoginInfo(userId, password);
                }
            });
            repo.login(new SysoutProgressMonitor());

            // Gather the necessary clients and managers necessary from the repository
            // In this case we need the source control workspace manager and the item manager
            IWorkspaceManager workspaceManager = (IWorkspaceManager) repo.getClientLibrary(IWorkspaceManager.class);
            IItemManager itemManager = repo.itemManager();
            IWorkItemClient workItemClient = (IWorkItemClient) repo.getClientLibrary(IWorkItemClient.class);
            ILinkManager linkManager = (ILinkManager) repo.getClientLibrary(ILinkManager.class);

            // Find the work item on the server
            int workItemNumber = -1;

            try
            {
                workItemNumber = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid work item nubmer:" + args[3]);
                System.exit(1);
            }

            IWorkItem workItem = workItemClient.findWorkItemById(workItemNumber, IWorkItem.FULL_PROFILE, new SysoutProgressMonitor());

            System.out.println("[" + workItem.getId() + "] " + workItem.getHTMLSummary().getPlainText());

            // Find all of the attached change sets using the link manager to find a special kind of
            // link that crosses between work items and source control change sets using its ID.
            IItemReference workItemReference = linkManager.referenceFactory().createReferenceToItem(workItem);
            ILinkCollection linkCollection = linkManager.findLinksByTarget(ILinkConstants.CHANGESET_WORKITEM_LINKTYPE_ID, workItemReference, new SysoutProgressMonitor()).getAllLinksFromHereOn();

            if (linkCollection.isEmpty())
            {
                System.out.println("Work item has no change sets.");
                System.exit(0);
            }

            List<IChangeSetHandle> changeSetHandles = new ArrayList<IChangeSetHandle>();

            for (ILink link : linkCollection)
            {
                // Change set links should be item references
                IChangeSetHandle changeSetHandle = (IChangeSetHandle) link.getSourceRef().resolve();

                changeSetHandles.add(changeSetHandle);
            }

            @SuppressWarnings ("unchecked")
            List<IChangeSet> changeSets = itemManager.fetchCompleteItems(changeSetHandles, IItemManager.DEFAULT, new SysoutProgressMonitor());
            Set<String> changedFilesAndFolders = new TreeSet<String>();
            for (IChangeSet cs : changeSets)
            {
                for (Object o : cs.changes())
                {
                    IChange change = (IChange) o;

                    if (change.kind() != IChange.DELETE)
                    {
                        IVersionableHandle after = change.afterState();

                        // Although versionable handles are item handles you cannot use the item
                        // manager to fetch the versionable from the handle. Instead, you use the
                        // versionable manager to do this.
                        IVersionable afterVersionable = workspaceManager.versionableManager().fetchCompleteState(after, new SysoutProgressMonitor());

                        changedFilesAndFolders.add(afterVersionable.getName());
                    }

                    // If there was a rename then include the old name in the list as well
                    if (change.kind() == IChange.RENAME || change.kind() == IChange.DELETE)
                    {
                        IVersionableHandle before = change.beforeState();

                        IVersionable beforeVersionable = workspaceManager.versionableManager().fetchCompleteState(before, new SysoutProgressMonitor());

                        changedFilesAndFolders.add(beforeVersionable.getName());
                    }
                }
            }

            for (String fileOrFilderName : changedFilesAndFolders)
            {
                System.out.println("\t" + fileOrFilderName);
            }
        }
        finally
        {
            TeamPlatform.shutdown();
        }
    }
}
