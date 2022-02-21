/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _input = input;
        _colName = colName;
        _match = match;
    }

    @Override
    protected boolean keep() {
        int int_val = _input.colNameToIndex(_colName);
        if (_next.getValue(int_val).equals(_match)) {
            return true;
        }
        return false;
    }

    Table _input;
    String _colName;
    String _match;
}
