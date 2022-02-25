import java.util.Random;
import java.util.concurrent.Semaphore;


public class Reindeer implements Runnable {

	public enum ReindeerState {AT_BEACH, AT_WARMING_SHED, AT_THE_SLEIGH};
	private ReindeerState state;
	private SantaScenario scenario;
	private Random rand = new Random();
	private Semaphore reindeerSemaphore;

	/**
	 * The number associated with the reindeer
	 */
	private int number;
	
	public Reindeer(int number, SantaScenario scenario, Semaphore reindeerSemaphore) {
		this.number = number;
		this.scenario = scenario;
		this.state = ReindeerState.AT_BEACH;
		this.reindeerSemaphore = reindeerSemaphore;
	}

	public ReindeerState getState() {
		return state;
	}

	/**
	 * Santa might call this function to fix the trouble
	 * @param state
	 */
	public void setState(ReindeerState state) {
		this.state = state;
	}

	@Override
	public void run() {
		while(true) {
			// terminate threads on day 370
			if (SantaScenario.day >= 370) {
				return;
			}
		// wait a day
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// see what we need to do:
		switch(state) {
		case AT_BEACH: { // if it is December, the reindeer might think about returning from the beach
			if (scenario.isDecember) {
				if (rand.nextDouble() < 0.1) {
					state = ReindeerState.AT_WARMING_SHED;
				}
			}
			break;			
		}
		case AT_WARMING_SHED: 
			for (Reindeer reindeer: scenario.reindeers) {
				if(reindeer.getState() == Reindeer.ReindeerState.AT_WARMING_SHED) {
					if(reindeerSemaphore.availablePermits() == 0) {
						scenario.santa.setState(Santa.SantaState.WOKEN_UP_BY_REINDEER);
					}
					else {
						try {
							this.reindeerSemaphore.acquire();
							setState(ReindeerState.AT_WARMING_SHED);
						}
						catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// if all the reindeer are home, wake up santa
			break;
		case AT_THE_SLEIGH: 
			// keep pulling
			break;
		}
		}
	};
	
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Reindeer " + number + " : " + state);
	}
	
}
