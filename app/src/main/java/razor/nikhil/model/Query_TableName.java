package razor.nikhil.model;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */


public class Query_TableName {
    private String query;

    public Query_TableName(String query, String tname) {
        this.query = query;
        this.tname = tname;
    }

    public Query_TableName() {
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    private String tname;
}

