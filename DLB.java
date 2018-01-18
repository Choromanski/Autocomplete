import java.util.ArrayList;

public class DLB{

    Node rootNode, searchNode;
    private final char END_CHAR = '*';

    public DLB(){
    }

    public void add(String input){
        input = input + END_CHAR;
        if(rootNode == null){
            this.addInitial(input);
        }else{
            Node currentNode = rootNode;
            for(int i = 0; i < input.length(); i++){
                if(currentNode.val == input.charAt(i) || (currentNode.childNode == null && currentNode.val != END_CHAR)){
                    if(currentNode.childNode == null){
                        currentNode.childNode = new Node(input.charAt(i), input);
                        currentNode = currentNode.childNode;
                    }else{
                        currentNode = currentNode.childNode;
                    }
                }else{
                    while(currentNode.siblingNode != null && currentNode.val != input.charAt(i)){
                        currentNode = currentNode.siblingNode;
                    }
                    if(currentNode.val == input.charAt(i)){
                        currentNode = currentNode.childNode;
                    }else if(currentNode.siblingNode == null){
                        currentNode.siblingNode = new Node(input.charAt(i), input);
                        currentNode = currentNode.siblingNode;
                    }
                }
            }
        }
    }

    public void addInitial(String input){
        rootNode = new Node(input.charAt(0), input);
        Node currentNode = rootNode;
        for(int i = 1; i < input.length(); i++){
            currentNode.childNode = new Node(input.charAt(i), input);
            currentNode = currentNode.childNode;
        }
    }

    public ArrayList<String> getSuggestions(char input){
        if(rootNode == null)
            return new ArrayList<String>();
        while(searchNode.siblingNode != null && searchNode.val != input){
            searchNode = searchNode.siblingNode;
        }
        if(searchNode.val == input){
            searchNode = searchNode.childNode;
        }else if(searchNode.val != input){
            return new ArrayList<String>();
        }
        return searchNode.getSuggestion();
    }

    public void resetSuggestions(){
        searchNode = rootNode;
    }

}
