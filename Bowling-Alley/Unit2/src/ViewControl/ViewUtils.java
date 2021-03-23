package ViewControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ViewUtils {
	public static JButton createButton(String text, JPanel panel, ActionListener actionListener) {
		JButton button = new JButton(text);
		JPanel tp = new JPanel();
		tp.setLayout(new FlowLayout());
		button.addActionListener(actionListener);
		tp.add(button);
		panel.add(tp);
		return button;
	}

	public static void setComponentProperties(Component component, int fontSize, int width, int height, int x, int y) {
		component.setFont(new Font("Arial", Font.PLAIN, fontSize));
		component.setSize(width, height);
		component.setLocation(x, y);
	}

	public static Object[][] parseMapToData(List<Map<String, String>> result, int numCols) {
		Object[][] res = new Object[result.size()][numCols];
		int i = 0;
		for (Map<String, String> map : result) {
			int j = 0;
			for (String key : map.keySet()) {
				res[i][j] = map.get(key);
				j++;
			}
			i++;
		}
		return res;
	}

	public static JComboBox createComboBox(int x, int y, Object[] content) {
		JComboBox box = new JComboBox(content);
		box.setFont(new Font("Arial", Font.PLAIN, 15));
		box.setSize(190, 20);
		box.setLocation(x, y);
		return box;
	}

	public static void setItemsToComboBox(Object[] items, JComboBox box) {
		box.removeAllItems();
		for (Object item : items) {
			box.addItem(item);
		}
	}
}
