FlucomaUnitTest : UnitTest {
	var <completed = false;
	var <result;
	var server;

	//Individual method test run
	*runTest { | method |
		var class, classInstance;
		# class, method = method.asString.split($:);
		class = class.asSymbol.asClass;
		method = class.findMethod(method.asSymbol);
		if(method.isNil) {
			Error(class.asString ++ ": test method not found: " ++ method).throw;
		};
		classInstance = class.new;
		classInstance.runTestMethod(method, false);
		^classInstance;
	}

	//per-method
	setUp {
		var uniqueId = UniqueID.next;
		completed = false;
		server = Server(
			this.class.name ++ uniqueId,
			NetAddr("127.0.0.1", 57110 + uniqueId)
		);
		server.bootSync;
	}

	//per-method
	tearDown {
		server.quit.remove;
		completed = true;
	}
}
