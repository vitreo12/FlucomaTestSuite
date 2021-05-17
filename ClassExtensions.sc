+ Array {
	not {
		this.do({ | entry |
			if(entry.not, { ^true })
		});
		^false;
	}
}