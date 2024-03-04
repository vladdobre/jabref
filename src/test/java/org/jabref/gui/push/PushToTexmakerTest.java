package org.jabref.gui.push;

import java.util.Map;

import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import org.jabref.gui.DialogService;
import org.jabref.logic.push.CitationCommandString;
import org.jabref.preferences.ExternalApplicationsPreferences;
import org.jabref.preferences.PreferencesService;
import org.jabref.preferences.PushToApplicationPreferences;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PushToTexmakerTest {

    private static final String TEXMAKER_CLIENT_PATH = "/usr/bin/texmaker";
    private static final String DISPLAY_NAME = "Texmaker";

    private PushToTexmaker pushToTexmaker;

    @BeforeEach
    public void setup() {
        DialogService dialogService = mock(DialogService.class, Answers.RETURNS_DEEP_STUBS);
        PreferencesService preferencesService = mock(PreferencesService.class);
        PushToApplicationPreferences pushToApplicationPreferences = mock(PushToApplicationPreferences.class);

        // Mock the command path
        Map<String, String> commandPaths = Map.of(DISPLAY_NAME, TEXMAKER_CLIENT_PATH);
        ObservableMap<String, String> observableCommandPaths = FXCollections.observableMap(commandPaths);
        when(pushToApplicationPreferences.getCommandPaths()).thenReturn(new SimpleMapProperty<>(observableCommandPaths));
        when(preferencesService.getPushToApplicationPreferences()).thenReturn(pushToApplicationPreferences);

        // Mock the return value for getCiteCommand()
        ExternalApplicationsPreferences externalApplicationsPreferences = mock(ExternalApplicationsPreferences.class);
        CitationCommandString mockCiteCommand = mock(CitationCommandString.class);
        when(mockCiteCommand.prefix()).thenReturn("");
        when(mockCiteCommand.suffix()).thenReturn("");
        when(externalApplicationsPreferences.getCiteCommand()).thenReturn(mockCiteCommand);
        when(preferencesService.getExternalApplicationsPreferences()).thenReturn(externalApplicationsPreferences);

        // Create a new instance of PushToTexmaker
        pushToTexmaker = new PushToTexmaker(dialogService, preferencesService);
    }

    /**
     * To verify that the PushToTexmaker class correctly returns its designated display name.
     * The display name is used to identify the application in the GUI.
     */
    @Test
    void displayName() {
        assertEquals(DISPLAY_NAME, pushToTexmaker.getDisplayName());
    }

    /**
     * To verify that the PushToTexmaker class correctly returns the command line for Texmaker.
     * The command line is used to execute the application from the command line.
     */
    @Test
    void getCommandLine() {
        String keyString = "TestKey";
        String[] expectedCommand = new String[] {null, "-insert", keyString}; // commandPath is only set in pushEntries

        String[] actualCommand = pushToTexmaker.getCommandLine(keyString);

        assertArrayEquals(expectedCommand, actualCommand);
    }

    /**
     * Check for the actual command and path with path is run.
     */
    @Test
    void pushEntries() {
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);

        String testKey = "TestKey";
        String[] expectedCommand = new String[] {TEXMAKER_CLIENT_PATH, "-insert", testKey};

        pushToTexmaker.pushEntries(null, null, testKey, processBuilder);

        verify(processBuilder).command(expectedCommand);
    }

    /**
     * To verify that the PushToTexmaker class correctly returns the tooltip for Texmaker.
     * The tooltip is used to display a short description of the application in the GUI.
     */
    @Test
    void getTooltip() {
        assertEquals("Push entries to external application (Texmaker)", pushToTexmaker.getTooltip());
    }
}
