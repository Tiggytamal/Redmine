package junit.control.rtc;

import org.junit.Test;

import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;

import control.rtc.ControlRTC;
import junit.JunitBase;

public class ControlRTCTest extends JunitBase
{
    @SuppressWarnings("deprecation")
    @Test
    public void connection() throws TeamRepositoryException
    {
        ControlRTC handler = new ControlRTC();
        handler.fetchAllProjectAreas(); 
        IQueryResult<IResolvedResult<IWorkItem>> results = handler.fetchItemById("PRJF_BF0598_DML_SIMM - Signature Libre", "309174");
        IResolvedResult<IWorkItem> itemResult = results.next(null);
        IWorkItem item = itemResult.getItem();
        System.out.println(item.getId());
        System.out.println(item.getTags());
        System.out.println(item.getWorkItemType());
        System.out.println(item.getCreationDate());
        System.out.println(handler.getWorkItemState(item));
    }
}