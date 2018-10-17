package hu.appropati.szunyog.input.text;

public interface TextInputProvider {
    enum InputType {
        TEXT, NUMBER
    }

    void registerListener(TextInputListener textInputListener);
    void removeListener(TextInputListener textInputListener);
    void openTextInput(String placeholder, String text, InputType type, int maxChars);
    void closeTextInput();
}
