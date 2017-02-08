/**
 * Este paquete contiene las implementaciones de los servlets de apoyo que se
 * utilizan para generar informaci�n de manera asincr�nica para la herramienta.
 */
package zinbig.item.util.servlets;

import java.text.ParseException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import zinbig.item.util.Constants;
import zinbig.item.util.StatisticsGeneratorJob;

/**
 * Este servlet se utiliza para iniciar una tarea peri�dica de Quartz que genera
 * estad�sticas de uso de la herramienta de manera asincr�nica as� la p�gina de
 * inicio solamente tiene que consultar la informaci�n ya generada en vez de
 * tener que hacer todas las consultas.
 * 
 * @author Javier Bazzocco javier.bazzocco@zinbig.com
 * 
 */
public class StatitisticsServlet extends HttpServlet {

	/**
	 * UID por defecto para la serializaci�n.
	 */
	private static final long serialVersionUID = 3761419068041672672L;

	/**
	 * Constructor por defecto.
	 * 
	 * @see HttpServlet#HttpServlet()
	 */
	public StatitisticsServlet() {
		super();
	}

	/**
	 * Inicializa este servlet para que arranque la tarea peri�dica.
	 * 
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		try {
			Scheduler sched = schedFact.getScheduler();

			sched.start();

			JobDetail aJobDetail = new JobDetail("statistics",
					Scheduler.DEFAULT_GROUP, StatisticsGeneratorJob.class);

			CronTrigger trigger = new CronTrigger("statsCron",
					Scheduler.DEFAULT_GROUP, "statistics",
					Scheduler.DEFAULT_GROUP, "0 45 13 * * ?");

			sched.scheduleJob(aJobDetail, trigger);
			this.getServletContext().setAttribute(Constants.SCHEDULER, sched);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Detiene la tarea iniciada en el m�todo init().
	 * 
	 */
	public void destroy() {
		try {

			Scheduler sched = (Scheduler) this.getServletContext()
					.getAttribute(Constants.SCHEDULER);
			sched.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		super.destroy();
	}

}
