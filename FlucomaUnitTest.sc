//Basic setup / teardown
FlucomaUnitTest : UnitTest {
	var server;

	setUp {
		server = Server(this.class.name);
		this.bootServer(server);
	}

	tearDown {
		server.quit.remove;
	}
}
