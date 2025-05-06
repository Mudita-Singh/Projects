class FitnessGoal:
    def __init__(self, description, target):
        self.description = description
        self.target = target

    def display(self):
        print(f"Fitness Goal: {self.description}")
        print(f"Target: {self.target}")

description = input("Enter fitness goal description: ")
target = input("Enter target for the goal: ")
goal = FitnessGoal(description, target)

# Calling the display method to show the goal
goal.display()