package analyzer;

import java.util.Objects;

public class Pattern implements Comparable<Pattern>{
    private String pattern;
    private String outputString;
    private int priority;

    public Pattern(String pattern, String outputString, int priority) {
        this.pattern = pattern;
        this.outputString = outputString;
        this.priority = priority;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getOutputString() {
        return outputString;
    }

    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern1 = (Pattern) o;
        return priority == pattern1.priority &&
                Objects.equals(pattern, pattern1.pattern) &&
                Objects.equals(outputString, pattern1.outputString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, outputString, priority);
    }


    @Override
    public int compareTo(Pattern o) {
        return Integer.compare(o.priority, this.priority);
    }
}
