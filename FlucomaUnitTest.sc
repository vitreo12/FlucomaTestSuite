FlucomaUnitTest : UnitTest {
	var <completed = false;
	var <result;
	var server;

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

	//completed works on a per-method basis
	setUp {
		completed = false;
		server = Server(
			this.class.name ++ UniqueID.next,
			NetAddr("127.0.0.1", 57110 + UniqueID.next)
		);
		server.bootSync;
	}

	tearDown {
		server.quit.remove;
		completed = true;
	}
}
