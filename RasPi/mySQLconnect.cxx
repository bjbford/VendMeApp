/*
 * mySQLconnect.cxx
 * 
 * Copyright 2018  <pi@raspberrypi>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
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


