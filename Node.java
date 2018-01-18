import java.util.ArrayList;

public class Node{

    char val;
    Node siblingNode = null, childNode = null;
    String word;

    public Node(char letter, String word){
        val = letter;
        this.word = word.substring(0, word.length()-1);
    }

    public ArrayList<String> getSuggestion(){
        ArrayList<String> suggestion = new ArrayList<String>();
        if(val == '*'){
            suggestion.add(word);
        }
        if(childNode != null){
            suggestion.addAll(childNode.getSuggestion());
        }
        if(siblingNode != null){
            suggestion.addAll(siblingNode.getSuggestion());
        }
        return suggestion;
    }
}
