//Basic setup / teardown
FlucomaUnitTest : UnitTest {
	var server;

	setUp {
		server = Server(this.class.name);
		this.bootSync(server);
	}

	tearDown {
		server.quit.remove;
	}
}
