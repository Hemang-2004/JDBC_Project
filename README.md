
# JDBC Project

This project is a Java database project which basically considers an SQL file where there is a database created and using Java and a terminal interface we can run the SQL queries using a menu based programme.


## Features

- All CRUD operations
- Interactive rollback which means that any where if any query is not executed ot deleted still it wont create problem as the transaction will roll back 
- Printing all the details and specific details if necessary 
- Easy to use

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.
## 🔗 Links
Link to my project -> [Link](https://github.com/Hemang-2004/JDBC_Project)


## Demo

```bash 
mysql -u root -p
```
this will set up the sql 
```bash
source db/tables.sql
source db/populate.sql
```
And then run the Java file using the run command 



## Authors

- [Hemang Seth](https://github.com/Hemang-2004)


## License

[MIT](https://choosealicense.com/licenses/mit/)

