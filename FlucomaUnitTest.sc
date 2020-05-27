FlucomaUnitTest : UnitTest {
	classvar <>logFile, <>completed = false;

	var server;

	*reset {
		this.superclass.reset;
		this.completed = false;
	}

	*report {
		Post.nl;
		if(failures.size > 0, {
			"There were failures:".inform;
			failures.do { arg results;
				results.report(true);
			};
		},  {
			"There were no failures".inform;
		});

		this.completed = true;
	}

	setUp {
		server = Server(this.class.name ++ UniqueID.next);
		server.bootSync;
	}

	tearDown {
		server.quit.remove;
	}
}
