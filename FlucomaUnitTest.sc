FlucomaUnitTest : UnitTest {
	classvar <>logFile;
	var <completed = false;

	var server;

	*reset {
		this.superclass.reset;
		this.completed = false;
	}

	*report {
		this.superclass.report;
		this.completed = true;
	}

	setUp {
		server = Server(this.class.name);
		bootSync(server);
	}

	tearDown {
		server.quit.remove;
	}
}
