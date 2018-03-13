package control.quartz;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import control.ControlSonar;
import utilities.Statics;
import utilities.TechnicalException;

public class JobAnomaliesSonar implements Job
{

	@Override
	public void execute(JobExecutionContext context)
	{
		try
		{
			ControlSonar handler = new ControlSonar(Statics.info.getPseudo(), Statics.info.getMotDePasse());
			handler.majFichierSuiviExcel();
			handler.majFichierSuiviExcelDataStage();
		} catch (InvalidFormatException | IOException e)
		{
			throw new TechnicalException("Erreur sur le job d'excetion", e);
		}
		
	}

}
