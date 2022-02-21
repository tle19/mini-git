/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        // FIXME: Add your code here.
        _input = input;
        _colName1 = colName1;
        _colName2 = colName2;
    }

    @Override
    protected boolean keep() {
        int int1 = _input.colNameToIndex(_colName1);
        int int2 = _input.colNameToIndex(_colName2);
        if (_next.getValue(int1).equals(_next.getValue(int2))) {
            return true;
        }
        return false;
    }
    Table _input;
    String _colName1;
    String _colName2;
}
