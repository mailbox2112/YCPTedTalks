package edu.ycp.cs320.aroby.db;

import java.util.Scanner;

import edu.ycp.cs320.aroby.persist.DatabaseProvider;
import edu.ycp.cs320.aroby.persist.DerbyDatabase;

public class InitDatabase {
	public static void init(Scanner keyboard) {

		DatabaseProvider.setInstance(new DerbyDatabase());
	}
}
