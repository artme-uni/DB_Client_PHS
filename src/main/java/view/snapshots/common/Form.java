package view.snapshots.common;

import view.components.panels.forms.EntryFormPanel;

import java.awt.event.ActionListener;
import java.util.List;

public interface Form {

    EntryFormPanel getEntryFormPanel();

    void clearForm();

    void addBackButtonListener(ActionListener actionListener);

    void addApplyButtonListener(ActionListener actionListener);

    List<Object> getValues();

    void clearInputData();
}
