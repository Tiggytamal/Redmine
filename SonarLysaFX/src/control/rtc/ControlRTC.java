package control.rtc;

import java.util.HashMap;
import java.util.Map;

public class ControlRTC
{
//    private ITeamRepository repo;
//    
//    public static void main(String... agrs) throws TeamRepositoryException
//    {
//        TeamPlatform.startup();
//        ControlRTC control = new ControlRTC();
//       control.connect("ETP8137", "28H02m89,;:!", "https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm");
//       Map<String, IProjectArea> map = control.fetchAllProjectAreas();
//       
//        
//    }
//
//    private ITeamRepository connect(final String user, final String password, String repoUrl)
//    {
//        ITeamRepository repository = TeamPlatform.getTeamRepositoryService().getTeamRepository(repoUrl);
//        repository.registerLoginHandler(new ITeamRepository.ILoginHandler() 
//        {
//            public ILoginInfo challenge(ITeamRepository repository)
//            {
//                return new ILoginInfo() {
//                    public String getUserId()
//                    {
//                        return user;
//                    }
//
//                    public String getPassword()
//                    {
//                        return password;
//                    }
//                };
//            }
//        });
//        repo = repository;
//        return repository;
//    }
//    
//    private Map<String, IProjectArea> fetchAllProjectAreas() throws TeamRepositoryException 
//    {
//
//          Map<String, IProjectArea> pareas = new HashMap<>();
//          IProcessItemService pis = (IProcessItemService) repo.getClientLibrary(
//              IProcessItemService.class);
//          for(Object pareaObj : pis.findAllProjectAreas(IProcessClientService.ALL_PROPERTIES, null)) {
//            /*
//             * On n'effectue pas de contrôle avec instanceof car findAllProjectAreas revoie toujours 
//             * des éléments de type IProjectArea
//             */
//            IProjectArea parea = (IProjectArea) pareaObj;
//            pareas.put(parea.getName(), parea);
//          }
//        return pareas;
//      }

}
