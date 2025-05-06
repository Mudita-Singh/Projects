
# USER PROFILE

class UserProfile:
    def __init__(self, name, age, weight, height):
        self.name = name
        self.age = age
        self.weight = weight
        self.height = height
       

    def display_profile(self):
        print(f"Name: {self.name}")
        print(f"Age: {self.age}")
        print(f"Weight: {self.weight} kg")
        print(f"Height: {self.height} cm")
       

def create_profile():
    name = input("Enter your name: ")
    age = int(input("Enter your age: "))
    weight = float(input("Enter your weight (kg): "))
    height = float(input("Enter your height (cm): "))

    return UserProfile(name, age, weight, height)

# Create a new profile
profile = create_profile()

# Display the profile details
profile.display_profile()


# GOAL SETTING

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


# WATER INTAKE

class WaterIntakeTracker:
    def __init__(self):
        self.intake = 0

    def track_intake(self, amount):
        self.intake += amount

    def display_intake(self):
        print(f"Water Intake: {self.intake} ml")

# Create an instance of WaterIntakeTracker
tracker = WaterIntakeTracker()

while True:
    # Track water intake
    amount = float(input("Enter amount of water intake (ml), or 0 to stop: "))
    if amount == 0:
        break  # Exit the loop if the user enters 0
    tracker.track_intake(amount)

# Display total intake
tracker.display_intake()
