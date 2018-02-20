/*
 * mySQLconnect.cxx
 * 
 * Function to establish connection to mySQL server.
 * 
 * Created on 2/17/18
 * @author bjbford 
 */


#include <iostream>
#include <mysql/mysql.h>

#define DB_HOST "mysql.cs.iastate.edu"
#define DB_SCHEMA "db309ss3"
#define DB_USERNAME "dbu309ss3"
#define DB_PASSWORD "aS!2DW2Q"
MYSQL *mysqlclient, *connection;

void mySQLconnect(void)
{
	mysqlclient = mysql_init(NULL);
	if(mysqlclient == NULL){
		fprintf(stderr,"%s\n",mysql_error(mysqlclient));
		return;
	}
	unsigned int port = 0;
	const char * socket = NULL;
	unsigned long clientFlag = 0;
	
	//Connect to our database
	connection = mysql_real_connect(mysqlclient,DB_HOST,DB_USERNAME,DB_PASSWORD,DB_SCHEMA,port,socket,clientFlag);
	if(connection != NULL){
		printf("Successful connection to your database!\n");
	}
	else{
		fprintf(stderr,"%s\n",mysql_error(mysqlclient));
	}
}

int main(int argc, char **argv)
{
	mySQLconnect();
	return 0;
}
