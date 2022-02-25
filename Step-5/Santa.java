//import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;

public class Santa implements Runnable {

	enum SantaState {SLEEPING, READY_FOR_CHRISTMAS, WOKEN_UP_BY_ELVES, WOKEN_UP_BY_REINDEER};
	private SantaState state;
	private SantaScenario scenario;
	
	public Santa(SantaScenario scenario) {
		this.state = SantaState.SLEEPING;
		this.scenario = scenario;
	}

	public void setState(SantaState state) {
		this.state = state;
	}
	
	@Override
	public void run() {
		while(true) {
			// terminate threads on day 370
			if (SantaScenario.day >= 370) {
				return;
			}
			// wait a day...
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch(state) {
			case SLEEPING: // if sleeping, continue to sleep
				break;
			case WOKEN_UP_BY_ELVES: 
				// check if their state is to get help, then change to working
				for (Elf elf : scenario.elves) {
					if(elf.getState() == Elf.ElfState.AT_SANTAS_DOOR) {
						if(scenario.elfSemaphore.availablePermits() == 0) {
							elf.setState(Elf.ElfState.WORKING);
							scenario.elfSemaphore.release();
						}
						else {
							elf.setState(Elf.ElfState.AT_SANTAS_DOOR);
						}
					}
				}
				scenario.santasDoor.clear();
				state = SantaState.SLEEPING;
				break;
			case WOKEN_UP_BY_REINDEER: 
				// assemble the reindeer to the sleigh then change state to ready
				for(Reindeer reindeer : scenario.reindeers) {
					if(reindeer.getState() == Reindeer.ReindeerState.AT_WARMING_SHED) {
						if(scenario.reindeerSemaphore.availablePermits() == 0) {
							reindeer.setState(Reindeer.ReindeerState.AT_THE_SLEIGH);
							scenario.reindeerSemaphore.release();
						}
						else {
							reindeer.setState(Reindeer.ReindeerState.AT_WARMING_SHED);
						}
					}
				}
				state = SantaState.READY_FOR_CHRISTMAS;
				break;
			case READY_FOR_CHRISTMAS: // nothing more to be done
				break;
			}
		}
	}

	
	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Santa : " + state);
	}
	
	
}
