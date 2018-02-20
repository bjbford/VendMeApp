/*
 * mySQLpost.cxx
 * 
 * Function to establish connection and post to mySQL server.
 * 
 * Created on 2/19/18
 * @author bjbford and jdanner
 */

#include <iostream>
#include <mysql_connection.h>
#include <cppconn/driver.h>
#include <cppconn/exception.h>
#include <cppconn/resultset.h>
#include <cppconn/prepared_statement.h>

#define DB_HOST "mysql.cs.iastate.edu"
#define DB_SCHEMA "db309ss3"
#define DB_USERNAME "dbu309ss3"
#define DB_PASSWORD "aS!2DW2Q"

void mySQLpost(void);

int main(int argc, char **argv)
{
	mySQLpost();
	return 0;
}

void mySQLpost(void){
	sql::Driver *drvr;
	sql::Connection *conn;
	sql::PreparedStatement *prep_state;
	
	drvr = get_driver_instance();
	conn = drvr->connect(DB_HOST,DB_USERNAME,DB_PASSWORD);
	conn->setSchema(DB_SCHEMA);
	
	prep_state = conn->prepareStatement("INSERT INTO info(Machine_Number,Location,Building,Contents,Type_Machine,Price) VALUES (?,?,?,?,?,?)");
	
	prep_state->setInt(1,2);													//Machine_Number
	prep_state->setString(2,"42.027134,-93.648371");							//Location
	prep_state->setString(3,"The Hub");											//Building
	prep_state->setString(4,"Coke, Diet Coke, Sprite, Cherry Coke, Powerade");	//Contents
	prep_state->setString(5,"Coca-Cola Machine");								//Type_Machine
	prep_state->setInt(6,125);													//Price
	prep_state->executeUpdate();
	printf("Posted!!\n");
	
	delete prep_state;
	delete conn;
}
