public class Firewall {
	RuleStore rules;

	public Firewall(String rules_fp) {
		rules = new RuleStore();
		populateRuleStore(rules_fp);
	}

	public populateRuleStore(String rules_fp) {
		/*
			Opens the relevant file and iterates over each line
			- for each line, call "addRule" on "rules" to add the rule
		*/
	}

	public boolean accept_packet(String direction, String protocol, int port, String ip_address) {
		/*
			concatenate parameters into a String[] and call "rules.check()"
		*/
	}
}