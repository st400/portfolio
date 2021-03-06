package name.abuchen.portfolio.bootstrap;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class CheckedListSelectionDialog extends Dialog
{
    private LabelProvider labelProvider;

    private String title;
    private String message = ""; //$NON-NLS-1$

    private Object[] elements;
    private Object[] selected;

    private CheckboxTableViewer tableViewer;

    public CheckedListSelectionDialog(Shell parentShell, LabelProvider labelProvider)
    {
        super(parentShell);
        this.labelProvider = labelProvider;

        setShellStyle(getShellStyle() | SWT.SHEET);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setElements(Collection<?> elements)
    {
        this.elements = this.selected = elements.toArray();
    }

    public Object[] getResult()
    {
        return selected != null ? selected : new Object[0];
    }

    @Override
    protected Control createContents(Composite parent)
    {
        Control contents = super.createContents(parent);
        getShell().setText(title);
        return contents;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite composite = (Composite) super.createDialogArea(parent);

        Composite container = new Composite(composite, SWT.None);
        GridDataFactory.fillDefaults().grab(true, true).hint(400, 300).applyTo(container);
        GridLayoutFactory.fillDefaults().numColumns(1).applyTo(container);

        Label label = new Label(container, SWT.None);
        label.setText(this.message);
        GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

        Composite tableArea = new Composite(container, SWT.NONE);
        GridDataFactory.fillDefaults().grab(false, true).applyTo(tableArea);
        tableArea.setLayout(new FillLayout());

        TableColumnLayout layout = new TableColumnLayout();
        tableArea.setLayout(layout);

        Table table = new Table(tableArea, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        tableViewer = new CheckboxTableViewer(table);
        table.setHeaderVisible(false);
        table.setLinesVisible(false);

        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.None);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100));

        tableViewer.setLabelProvider(labelProvider);
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        tableViewer.setInput(elements);
        tableViewer.setCheckedElements(elements);

        tableViewer.setComparator(new ViewerComparator());

        hookListener();

        return composite;
    }

    private void hookListener()
    {
        tableViewer.addCheckStateListener(new ICheckStateListener()
        {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event)
            {
                selected = tableViewer.getCheckedElements();
            }
        });
    }
}
