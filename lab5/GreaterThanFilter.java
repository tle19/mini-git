/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _input = input;
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        int int_val = _input.colNameToIndex(_colName);
        if (_next.getValue(int_val).length() > _ref.length()) {
            return true;
        }
        return false;
    }

    Table _input;
    String _colName;
    String _ref;
}
