public interface Rule {
	public boolean check(String[] params, int idx);
}

public class RuleStore implements Rule {
	private List<Direction> directions;

	public RuleStore() {
		directions = new ArrayList<Direction>();
	}

	public void addRule(String rule) {
		/*
			Parses the rule and adds it to "directions"
		*/
	}

	public void check(String[] params, int idx) {
		/* 
			checks each element of "directions" to see if "params[idx]"
			matches
			- if match: cascade the check to the corresponding Direction object with idx+1
			- else return false
		*/
	}
}


public class Direction implements Rule {
	private Dir dir;
	private List<Protocol> protocols;

	public Direction(String dir) {
		// assumes dir MUST correspond to one of the types of Dir
		this.dir = Dir.valueOf(dir.toUpperCase());
		initProtocolList();
	}

	public Direction(String dir, String[] params) {
		this(dir);
		initProtocols(params);
	}

	private void initProtocolList() {
		protocols = new ArrayList<Protocol>();
	}

	public void (String[] params) {
		/*
			Creates new Protocol object if the protocol type doesn't
			exist yet in "protocols" or initializes the currently existing
			object with the rule's new Port and IP range 
		*/
	}

	public boolean check(String[] params, int idx) {
		/* 
			checks each element of "protocols" to see if "params[idx]"
			matches
			- if match: cascade the check to the corresponding Protocol object with idx+1
			- else return false
		*/
	}
}

public class Protocol implements Rule {
	private Proto proto;
	private Queue<Port> ports;

	public Protocol(String proto) {
		// assumes proto MUST correspond to one of the types of Proto
		this.proto = Protocol.valueOf(proto.toUpperCase());
		initPortsQueue();
	}

	public Protocol(String proto, String[] initVals) {
		this(proto);
		initPorts(initVals);
	}

	private void initPortsQueue() {
		/*
			make comparator for Port that compares based on the "start" of the
			Port's range
		*/
	}

	public void initPorts(String[] initVals) {
		/*
			Similar to initialization of "initIpRange" in the "Port" class

			If there's a port in "ports" that has the same "start" and "end" port range,
			add the new ip address range to that element. Otherwise, create a new Port
		*/
	}

	public boolean check(String[] params, int idx) {
		/* 
			iterates over "ports" and checks if params[idx] (assumed to be the port)
			is within the range of the element of "ports"
			- if match: cascade the check to the corresponding Port object with idx+1 to check Ips
			- else continue iterating

			return false if no matches
		*/
	}
}

public class Port implements Rule {
	private int start;
	private int end;
	private Queue<IpAddress> addresses;

	public Port(int start, int end) {
		this.start = start;
		this.end = end;
		initAddresses();
	}	

	public Port(int start, int end, String ipRange) {
		this(start, end);
		initIpRange(ipRange);
	}

	public void initIpRange(String ipRange) {
		String[] ipBounds = ipRange.split("-");

		String ipStart = ipBounds[0];
		String ipEnd = ipBounds[0];
		if (ipBounds.length > 1) {
			ipEnd = ipBounds[1];
		}
		IPAddress ipAddr = new IpAddress(ipStart, ipEnd);
		/*
			check if the "addresses" queue contains the same first octet as "ipStart"
			- if so, add "ipAddr" to that address tree
			- otherwise, add "ipAddr" as a new element of "addresses"
		*/
	}

	private initAddresses() {
		addresses = new PriorityQueue<IpAddress>(new Comparator<IpAddress>() {
			@Override
			public int compare(IpAddress ip1, IpAddress ip2) {
				char ip1Val = ip1.getStartPrefix();
				char ip2Val = ip2.getStartPrefix();
				if (ip1Val < ip2Val) {
					return -1;
				} else if (ip1Val > ip2Val) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	public boolean check(String[] params, int idx) {
		/* 
			iterates over "addresses" and checks if params[idx] (assumed to be the IP address)
			is within the range of the element in "addresses"

			return false if no matches
		*/
	}
}

public class IpAddress implements Rule {
	private IpOctet start;
	private IpOctet end;

	public IpAddress(String start, String end) {
		this.start = initIpOctet(start);
		this.end = initIpOctet(end);
	}

	private IpOctet initIpOctet(String address) {
		String[] stringOctets = address.split(".");
		char[] octetValues = new char[stringOctets.length];

		for (int i=0; i < octetValues.length; i++) {
			int val = Integer.parseInt(stringOctets[i]);
			octetValues[i] = val;
		}

		return new IpOctet(octetValues, 0);
	}

	public boolean check(String[] params, int idx) {
		/*
			checks if FIRST octet of params[idx] is within the range of the 
			"start" and "end" IpOctets
			- if within range but not equal to bounds: return true
			- if equal to either bound: cascade check to subsequent octets
			- if not in bounds: continue iterating

			return false if no matches
		*/
	}

	public char getStartPrefix() {
		return start.getOctetValue();
	}
}

public class IpOctet {
	private char octetValue;
	private Map<Character, IpOctet> children;

	public IpOctet(char octetValue) {
		this.octetValue = octetValue;
		children = new TreeMap<Character, IpOctet>();
	}

	public IpOctet(char[] octetValues, int idx) {
		this(octetValues[idx]);
		initChildren(octetValues, idx);
	}

	public void initChildren(char[] octetValues, int idx) {
		if (idx + 1 >= octetValues.length)	return;

		IpOctet child;
		Character childVal = new Character(octetValues[idx+1]);

		if (children.containsKey(childVal)) {
			child = children.get(childVal);
			child.initChildren(octetValues, idx+1);
		} else {
			child = new IpOctet(octetValues, idx+1);
		}

		children.put(childVal, child);
	}

	public boolean checkMatch(char[] octets, int idx) {
		/*
			checks if "octects[idx]" is >= the KEYs of "children"
			- if so, check if "end" of the matched IpOctet is >= "octets[idx]"
			- follow similar logic from "check()" in IpAddress

			return false if no matches
		*/	
	}

	public char getOctetValue() {
		return octetValue;
	}
}


public enum Dir {
	INBOUND, OUTBOUND
}

public enum Proto {
	TCP, UDP
}