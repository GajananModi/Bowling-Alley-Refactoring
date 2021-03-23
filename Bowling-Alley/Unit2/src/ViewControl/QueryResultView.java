package ViewControl;

import javax.swing.*;

public class QueryResultView {
    JFrame f;

    public QueryResultView(Object[] column, Object[][] data) {
        f = new JFrame();
        JTable jt = new JTable(data, column);
        jt.setBounds(30, 40, 200, 300);
        JScrollPane sp = new JScrollPane(jt);
        f.add(sp);
        f.setSize(300, 400);
        f.setVisible(true);
    }
}