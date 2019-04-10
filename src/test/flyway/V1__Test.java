package test.flyway;

import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

public class V1__Test extends BaseJavaMigration {
	@Override
	public void migrate(Context context) throws Exception {
		try (Statement select = context.getConnection().createStatement()) {
			try (ResultSet rows = select.executeQuery("SELECT USER_NO FROM TZF_USER_M ODER BY USER_NO DESC")) {
				while (rows.next()) {
					String userNo = rows.getString(1);
					System.out.println(userNo);
					/*
					String anonymizedName = "Anonymous" + id;
					try (Statement update = connection.createStatement()) {
						update.execute("UPDATE person SET name='" + anonymizedName + "' WHERE id=" + id);
					}*/
				}
			}
		}
	}
}