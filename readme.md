# The Supermarket Receipt Refactoring Kata

This is a variation of a popular kata described in http://codekata.com/kata/kata01-supermarket-pricing/. 

The aim of the exercise is to: 
- write automated tests for this code (can be skipped) and/or
- refactor it and then add a new feature.

The supermarket has a catalog with different types of products (rice, apples, milk, toothbrushes,...). 
Each product has a price, and the total price of the shopping cart is the total of all the prices of the items.
You get a receipt that details the items you've bought, the total price and any discounts that were applied.

The supermarket runs special deals, e.g.
- Buy two toothbrushes, get one free. Normal toothbrush price is €0.99
- 20% discount on apples, normal price €1.99 per kilo.
- 10% discount on rice, normal price €2.49 per bag
- Five tubes of toothpaste for €7.49, normal price €1.79
- Two boxes of cherry tomatoes for €0.99, normal price €0.69 per box.

These are just examples: the actual special deals change each week.

## Task 1: Write Unit Tests (can be skipped)

Create some test cases and aim to get good enough code coverage that you feel confident to do some refactoring.

Start from SupermarketTestTODO, and delete the other tests in src/test.
When you have good test cases, move to the next phase.

## Task 2: Refactoring

If you want to skip the previous step, you can use pre-written tests:
1. SupermarketTestDONE covers the discount logic.
2. GoldenMasterTest covers also the ReceiptPrinter by comparing the output with the one "recorded from production". To use this test, you must RUN it once **BEFORE** changing any line of code. Running the test the first time will generate a series of output files that will be then used to check the behavior stayed the same.

Identify code smells such as Long Method, Feature Envy. Apply relevant refactorings.

When you're confident you can handle this code, implement the new feature described below

## Task 3: New feature: discounted bundles

The owner of the system has a new feature request. They want to introduce a new kind of special offer - bundles. 
When you buy all the items in a product bundle
you get 10% off the total for those items. For example you could make a bundle offer of one toothbrush and one toothpaste.
If you then you buy one toothbrush and one toothpaste, the discount will be 10% of €0.99 + €1.79. 
If you instead buy two toothbrushes and one toothpaste, you get the same discount as if you'd bought only one of each -
ie only complete bundles are discounted.

Of course, you should add the appropriate tests along with the changes you do. Tip: use SupermarketKataDONE class

## Task 4: New feature: HTML receipt - design only

Currently we print a traditional ticket receipt. Now being a modern business we'd
like to be able to print or send an HTML version of the same receipt. All the data
and number formatting should be the same. However, the layout should be html.
You don't have to worry about the HTML template - a designer will care of that - but
we do need someone to keep duplication between the reports to a bare minimum.

