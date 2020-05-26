FlucomaUnitTest : UnitTest {
	classvar <completed = false;

	var server;

	*reset {
		this.superclass.reset;
		completed = false;
	}

	*report {
		this.superclass.report;
		completed = true;
	}

	setUp {
		server = Server(this.class.name);
		bootSync(server);
	}

	tearDown {
		server.quit.remove;
	}
}
