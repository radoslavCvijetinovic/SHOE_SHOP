/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.db;

import com.rcs.shoe.shop.common.exceptions.ShoeShopException;
import com.rcs.shoe.shop.common.exceptions.ShoeShopExceptionsEnum;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rajko
 */
public class LiquibaseExecutor {

    private static final Logger LOOGER = LoggerFactory.getLogger(LiquibaseExecutor.class);

    private static final String DB_CHANGE_LOG = "db/db-changelog.xml";

    private Liquibase liquibase;

    private final Connection connection;

    public LiquibaseExecutor(DataSource dataSource) throws ShoeShopException {

        try {
            connection = dataSource.getConnection();

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            liquibase = new Liquibase(DB_CHANGE_LOG, new ClassLoaderResourceAccessor(), database);

        } catch (DatabaseException ex) {
            LOOGER.error("LiquibaseExecutor", ex);
            throw new ShoeShopException(ShoeShopExceptionsEnum.DB_EXCEPTION.setCause(ex));
        } catch (LiquibaseException ex) {
            LOOGER.error("LiquibaseExecutor", ex);
            throw new ShoeShopException(ShoeShopExceptionsEnum.DB_EXCEPTION.setCause(ex));
        } catch (SQLException ex) {
            LOOGER.error("LiquibaseExecutor", ex);
            throw new ShoeShopException(ShoeShopExceptionsEnum.DB_EXCEPTION.setCause(ex));
        }

    }

    public void update() throws ShoeShopException {

        try {
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException ex) {
            LOOGER.error("LiquibaseExecutor update", ex);
            throw new ShoeShopException(ShoeShopExceptionsEnum.DB_EXCEPTION.setCause(ex));
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOOGER.error("LiquibaseExecutor", ex);
                    throw new ShoeShopException(ShoeShopExceptionsEnum.DB_EXCEPTION.setCause(ex));
                }
            }
        }

    }

}
