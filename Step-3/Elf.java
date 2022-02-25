import java.util.Random;

public class Elf implements Runnable {

	enum ElfState {
		WORKING, TROUBLE, AT_SANTAS_DOOR
	};

	private ElfState state;
	/**
	 * The number associated with the Elf
	 */
	private int number;
	private Random rand = new Random();
	private SantaScenario scenario;
	private int elfNum;

	public Elf(int number, SantaScenario scenario) {
		this.number = number;
		this.scenario = scenario;
		this.state = ElfState.WORKING;
	}

	public ElfState getState() {
		return state;
	}

	/**
	 * Santa might call this function to fix the trouble
	 * @param state
	 */
	public void setState(ElfState state) {
		this.state = state;
	}

	@Override
	public void run() {
		while (true) {
			// terminate threads on day 370
			if (SantaScenario.day >= 370) {
				return;
			}
      // wait a day
  		try {
  			Thread.sleep(100);
  		} catch (InterruptedException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
			switch (state) {
			case WORKING: {
				// at each day, there is a 1% chance that an elf runs into
				// trouble.
				if (rand.nextDouble() < 0.01) {
					state = ElfState.TROUBLE;
				}
				break;
			}
			case TROUBLE:
				for (Elf elf: scenario.elves) {
					if(elf.getState() == Elf.ElfState.TROUBLE) {
						elfNum++;
					}
				}
				if(elfNum >= 3) {
					for(Elf elf: scenario.elves) {
						if(elf.getState() == Elf.ElfState.TROUBLE) {
							elf.setState(Elf.ElfState.AT_SANTAS_DOOR);
						}
					}
				}
				break;
			case AT_SANTAS_DOOR:
				// wake up Santa
				scenario.santa.setState(Santa.SantaState.WOKEN_UP_BY_ELVES);
				break;
			}
		}
	}

	/**
	 * Report about my state
	 */
	public void report() {
		System.out.println("Elf " + number + " : " + state);
	}

}
