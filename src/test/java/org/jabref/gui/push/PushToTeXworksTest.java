package org.jabref.gui.push;

import java.util.Map;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import org.jabref.gui.DialogService;
import org.jabref.logic.push.CitationCommandString;
import org.jabref.preferences.ExternalApplicationsPreferences;
import org.jabref.preferences.PreferencesService;
import org.jabref.preferences.PushToApplicationPreferences;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PushToTeXworksTest {

    private PushToTeXworks pushToTeXworks;

    @BeforeEach
    public void setup() {
        // Mock the DialogService and PreferencesService
        DialogService dialogService = mock(DialogService.class, Answers.RETURNS_DEEP_STUBS);
        PreferencesService preferencesService = mock(PreferencesService.class);
        PushToApplicationPreferences pushToApplicationPreferences = mock(PushToApplicationPreferences.class);
        ExternalApplicationsPreferences externalApplicationsPreferences = mock(ExternalApplicationsPreferences.class);

        // Mock the return value for getCommandPaths()
        String teXworksClientPath = "/usr/bin/texworks";
        String displayName = "TeXworks";

        Map<String, String> commandPaths = Map.of(displayName, teXworksClientPath);
        ObservableMap<String, String> observableCommandPaths = FXCollections.observableMap(commandPaths);
        when(pushToApplicationPreferences.getCommandPaths()).thenReturn(new SimpleMapProperty<>(observableCommandPaths));
        when(preferencesService.getPushToApplicationPreferences()).thenReturn(pushToApplicationPreferences);

        // Mock the return value for getCiteCommand()
        CitationCommandString mockCiteCommand = mock(CitationCommandString.class);
        when(mockCiteCommand.prefix()).thenReturn("");
        when(mockCiteCommand.suffix()).thenReturn("");
        when(externalApplicationsPreferences.getCiteCommand()).thenReturn(mockCiteCommand);

        // Mock the return value for getExternalApplicationsPreferences()
        when(preferencesService.getExternalApplicationsPreferences()).thenReturn(externalApplicationsPreferences);

        // Create a new instance of PushToTeXworks
        pushToTeXworks = new PushToTeXworks(dialogService, preferencesService);

        // Verify that the command path is correctly set in the preferences
        MapProperty<String, String> actualCommandPaths = pushToApplicationPreferences.getCommandPaths();
        Map<String, String> actualMap = actualCommandPaths.get();
        String actualPath = actualMap.get(displayName);
        assertEquals(teXworksClientPath, actualPath);
    }

    /**
     * To verify that the PushToTeXworks class correctly returns its designated display name.
     * The display name is used to identify the application in the GUI.
     *
     * Method: getDisplayName()
     *
     */
    @Test
    void displayName() {
        assertEquals("TeXworks", pushToTeXworks.getDisplayName());
    }

    /**
     * To verify that the PushToTeXworks class correctly returns the command line for TeXworks.
     * The command line is used to execute the application from the command line.
     *
     * Method: getCommandLine()
     *
     */
    @Test
    void getCommandLine() {
        String keyString = "TestKey";
        String[] expectedCommand = new String[] {"/usr/bin/texworks", "--insert-text", keyString};

        pushToTeXworks.setCommandPath("/usr/bin/texworks");

        String[] actualCommand = pushToTeXworks.getCommandLine(keyString);

        assertArrayEquals(expectedCommand, actualCommand);
    }

    /**
     * This test run the external application to push the keys to be cited
     *
     * Method: pushEntries()
     *
     * [Precondition]: TeXworks must run in the background
     */
    @Disabled("Disabled as it needs running TeXworks in the background. Start TeXworks with &")
    @Test
    void pushEntries() {
        pushToTeXworks.pushEntries(null, null, "key1,key2");
    }

    /**
     * To verify that the PushToTeXworks class correctly returns the tooltip for TeXworks.
     * The tooltip is used to display a short description of the application in the GUI.
     *
     * Method: getTooltip()
     *
     */
    @Test
    void getTooltip() {
        assertEquals("Push entries to external application (TeXworks)", pushToTeXworks.getTooltip());
    }
}
