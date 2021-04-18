import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

//        public static void main(String args[]) {
//            Scanner in = new Scanner(System.in);
//
//            Deque<Action> actionsToMake = new LinkedList<>();
//            // game loop
//            while (true) {
//                List<Action> castActionList = new ArrayList<>();
//                Map<Action, Integer> brewActionPrice = new HashMap<>();
//
//                int actionCount = in.nextInt(); // the number of spells and recipes in play
//                for (int i = 0; i < actionCount; i++) {
//                    int actionId = in.nextInt(); // the unique ID of this spell or recipe
//
//                    String actionType = in.next(); // in the first league: BREW; later: CAST, OPPONENT_CAST, LEARN, BREW
//                    int delta0 = in.nextInt(); // tier-0 ingredient change
//                    int delta1 = in.nextInt(); // tier-1 ingredient change
//                    int delta2 = in.nextInt(); // tier-2 ingredient change
//                    int delta3 = in.nextInt(); // tier-3 ingredient change
//
//                    int price = in.nextInt(); // the price in rupees if this is a potion
//                    Action action = new Action(actionId, actionType, Arrays.asList(delta0, delta1, delta2, delta3));
//
//                    if (actionType.equalsIgnoreCase("BREW")) {
//                        brewActionPrice.put(action, price);
//                    } else if (actionType.equalsIgnoreCase("CAST")) {
//                        castActionList.add(action);
//                    }
//
//                    int tomeIndex = in.nextInt(); // in the first two leagues: always 0; later: the index in the tome if this is a tome spell, equal to the read-ahead tax; For brews, this is the value of the current urgency bonus
//                    int taxCount = in.nextInt(); // in the first two leagues: always 0; later: the amount of taxed tier-0 ingredients you gain from learning this spell; For brews, this is how many times you can still gain an urgency bonus
//                    boolean castable = in.nextInt() != 0; // in the first league: always 0; later: 1 if this is a castable player spell
//                    boolean repeatable = in.nextInt() != 0; // for the first two leagues: always 0; later: 1 if this is a repeatable player spell
//                }
//
//                Inventory currentInventory = new Inventory();
//                Inventory enemyInventory = new Inventory();
//                for (int i = 0; i < 2; i++) {
//                    int inv0 = in.nextInt(); // tier-0 ingredients in inventory
//                    int inv1 = in.nextInt();
//                    int inv2 = in.nextInt();
//                    int inv3 = in.nextInt();
//                    if (i == 0) {
//                        currentInventory = new Inventory(Arrays.asList(inv0, inv1, inv2, inv3));
//                    } else {
//                        enemyInventory = new Inventory(Arrays.asList(inv0, inv1, inv2, inv3));
//                    }
//                    int score = in.nextInt(); // amount of rupees
//                }
//
//                //find max
//                Map.Entry<Action, Integer> maxEntry = null;
//                for (Map.Entry<Action, Integer> entry : brewActionPrice.entrySet()) {
//                    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
//                        maxEntry = entry;
//                    }
//                }
//                Recipe currentTargetRecipe = new Recipe(maxEntry.getKey());
//
//
//                if(actionsToMake.isEmpty() && LogicEngine.isInventorySubsetOfRecipe(currentInventory, currentTargetRecipe, true)) {
//                    actionsToMake = LogicEngine.findActionsToGetRemainingIngredients(currentInventory, currentTargetRecipe, castActionList);
//                    System.out.println(actionsToMake.pop());
//                }else if (actionsToMake.isEmpty()){
//                    System.out.println("BREW " + maxEntry.getKey().actionId);
//                }else{
//                    System.out.println(actionsToMake.pop());
//                }
//
//
//
//                // in the first league: BREW <id> | WAIT; later: BREW <id> | CAST <id> [<times>] | LEARN <id> | REST | WAIT
//    //            System.out.println("BREW " + actionIDList.get(maxPriceIndex));
//
//            }
//        }

    public static void main(String[] args) {
        Inventory inventory = new Inventory(List.of(0, 0, 0, 0));
        Action get0 = new Action(78, "CAST", List.of(2, 0, 0, 0));
        Action get1 = new Action(79, "CAST", List.of(-1, 1, 0, 0));
        Action get2 = new Action(80, "CAST", List.of(0, -1, 1, 0));
        Action get3 = new Action(81, "CAST", List.of(0, 0, -1, 1));

        List<Action> actionList = List.of(get0, get1, get2, get3);
        ////        System.out.println(LogicEngine.castSpellForIngredient(get0, inventory));
        ////        System.out.println(LogicEngine.castSpellForIngredient(get1, inventory));
        //
        Recipe sample = new Recipe(List.of(0, 0, 3, 2));

        //        System.out.println(LogicEngine.isInventorySubsetOfRecipe(inventory, sample));
        //
        //
        Deque<Action> actionsToMake = LogicEngine.findActionsToGetRemainingIngredients(inventory, sample, actionList);
        System.out.println(actionsToMake);
    }

}

class LogicEngine {

    private static int findIndexToRemoveLeadingZeroes(String string) {
        int index;
        for (index = 0; index < string.length(); index++) {
            if (string.charAt(index) != '0') {
                break;
            }
        }
        return index;
    }

    private static int subtractInventoryFromRecipe(Inventory currentInventory, Recipe recipe) {
        StringBuilder recipeNumber = new StringBuilder();
        StringBuilder inventoryNumber = new StringBuilder();

        for (int i = 0; i < recipe.ingredientCost.size(); i++) {
            recipeNumber.append(recipe.ingredientCost.get(i));
            inventoryNumber.append(currentInventory.inventory.get(i));
        }
        int leadingZeroesIndex = findIndexToRemoveLeadingZeroes(recipeNumber.toString());



        String builtRecipeNumber = recipeNumber.toString().substring(leadingZeroesIndex);
        String builtInventoryNumber = inventoryNumber.toString().substring(leadingZeroesIndex);

        return Integer.parseInt(builtRecipeNumber) - Integer.parseInt(builtInventoryNumber);
    }

    public static Deque<Action> findActionsToGetRemainingIngredients(Inventory currentInventory, Recipe recipe, List<Action> availableActions) {
        Deque<Action> actionsToMake = new LinkedList<>();
        while (isInventorySubsetOfRecipe(currentInventory, recipe, true)) {
            if (!isEnoughNumberOfBaseIngredient(currentInventory, recipe)) {
                actionsToMake.add(availableActions.get(0));
                currentInventory = castSpellForIngredient(availableActions.get(0), currentInventory);
            }
            for (int i = 1; i < availableActions.size(); i++) {
                Inventory experimentalInventory = castSpellForIngredient(availableActions.get(i), currentInventory);
                if (isInventorySubsetOfRecipe(experimentalInventory, recipe, false)) {
                    actionsToMake.add(availableActions.get(i));
                    currentInventory = new Inventory(experimentalInventory);
                }
            }
            actionsToMake.add(new Action("REST"));

        }
        return actionsToMake;

    }

    //check if the ingredients in inventory would all be used up for recipe (including after transformations)
    public static boolean isInventorySubsetOfRecipe(Inventory currentInventory, Recipe recipe, boolean isProperSubset) {
        if (currentInventory.isEmpty()) {
            return true;
        }

        int result = subtractInventoryFromRecipe(currentInventory, recipe);
        if (isProperSubset) {
            return (result > 0);
        }
        return (result >= 0);
    }

    public static Inventory castSpellForIngredient(Action action, Inventory currentInventory) {
        Inventory copy = new Inventory(currentInventory);
        for (int i = 0; i < copy.inventory.size(); i++) {
            copy.addDeltaToInventory(action.delta.get(i), i);
        }
        return copy;
    }

    //check if we have enough amount of ingredients
    private static boolean isEnoughNumberOfBaseIngredient(Inventory currentInventory, Recipe recipe) {
        //It could be that we already have higher tier ingredients
        // than in recipe in which case it should not be counted.
        int maxIndexToLook = findMaxIndexToLook(recipe);
        LongAdder adder = new LongAdder();
        List<Integer> currInventoryToCheck = currentInventory.inventory.subList(0, maxIndexToLook + 1);
        currInventoryToCheck.parallelStream().forEach(adder::add);
        return adder.intValue() >= recipe.ingredientCost.stream().mapToInt(Integer::intValue).sum();
    }

    private static int findMaxIndexToLook(Recipe recipe) {
        int maxIndexToLook = recipe.ingredientCost.size() - 1;
        for (int i = recipe.ingredientCost.size() - 1; i > 0; i--) {
            if (recipe.ingredientCost.get(i) != 0) {
                maxIndexToLook = i;
                break;
            }
        }
        return maxIndexToLook;
    }

    private static List<Integer> findMissingIngredient(Inventory currentInventory, Recipe recipe) {
        Inventory copy = new Inventory(currentInventory);
        List<Integer> difference = new ArrayList<>();
        for (int i = 0; i < recipe.ingredientCost.size(); i++) {
            difference.add(recipe.ingredientCost.get(i) - copy.inventory.get(i));
        }
        difference = difference.stream().filter(e -> e < 0).collect(Collectors.toList());
        return difference;
    }
}

class Inventory {
    List<Integer> inventory; // contains amount of tier 0 - 3 ingredients, only has 4 slots
    final int maxSize = 10;

    public Inventory() {

    }

    public Inventory(List<Integer> inventory) {
        this.inventory = inventory;
        if (this.inventory.size() > 4) {
            throw new IllegalArgumentException("Too many tier");
        }
    }

    public Inventory(Inventory other) {
        this.inventory = new ArrayList<>(other.inventory);

    }

    public void addDeltaToInventory(int delta, int index) {
        if (index > 3) {
            throw new IllegalArgumentException("Out of Bound");
        }
        int tmp = this.inventory.get(index);
        if (tmp + delta < 0) {
            return;
        }
        this.inventory.set(index, this.inventory.get(index) + delta);
    }

    public boolean isEmpty() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Inventory{" + "inventory=" + inventory + "}";
    }
}

class Action {
    int actionId;
    String actionType;
    List<Integer> delta;

    public Action(int actionId, String actionType, List<Integer> delta) {
        this.actionId = actionId;
        this.actionType = actionType;
        this.delta = delta;
    }

    public Action(String actionType) {
        if (actionType.equalsIgnoreCase("rest")) {
            this.actionType = actionType;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(actionType);
        sb.append(" ");
        if (actionId != 0) {
            sb.append(actionId);
        }
        return sb.toString();
    }
}

class Recipe {
    List<Integer> ingredientCost;

    public Recipe(List<Integer> ingredientCost) {
        this.ingredientCost = ingredientCost;
    }

    public Recipe(Action brewAction) {
        if (!brewAction.actionType.equalsIgnoreCase("BREW")) {
            throw new IllegalArgumentException("not brew action");
        }
        ingredientCost = new ArrayList<>();
        for (int i = 0; i < brewAction.delta.size(); i++) {
            if (brewAction.delta.get(i) <= 0) {
                ingredientCost.add(Math.abs(brewAction.delta.get(i)));
            }
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Recipe{");
        sb.append("ingredientCost=").append(ingredientCost);
        sb.append('}');
        return sb.toString();
    }
}

