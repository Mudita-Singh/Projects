import pandas as pd
import matplotlib.pyplot as plt
from pandas.plotting import register_matplotlib_converters

# Register converters for datetime plotting
register_matplotlib_converters()

def track_menstrual_cycle():
    # Initialize an empty list to store menstrual cycle data
    menstrual_data = []

    while True:
        start_date = input("Enter the start date of your period (YYYY-MM-DD), or 'done' to finish: ")
        
        if start_date.lower() == 'done':
            break

        try:
            # Input the duration of the period
            duration = int(input("Enter the duration of your period (in days): "))
            # Append the menstrual cycle data to the list
            menstrual_data.append({'Start Date': start_date, 'Duration': duration})
        except ValueError:
            print("Invalid input. Please enter a valid date and duration.")

    return menstrual_data

def visualize_menstrual_cycle(menstrual_data):
    # Create a DataFrame from the menstrual cycle data
    df = pd.DataFrame(menstrual_data)
    
    # Convert 'Start Date' column to datetime format
    df['Start Date'] = pd.to_datetime(df['Start Date'])

    # Create a calendar plot
    plt.figure(figsize=(10, 6))
    plt.plot(df['Start Date'], df['Duration'], marker='o', linestyle='None', color='red')
    plt.title('Menstrual Cycle Tracking')
    plt.xlabel('Date')
    plt.ylabel('Duration (days)')
    plt.grid(True)
    plt.tight_layout()
    plt.show()

# Track menstrual cycle data
menstrual_data = track_menstrual_cycle()

# Visualize menstrual cycle data on a calendar
if menstrual_data:
    visualize_menstrual_cycle(menstrual_data)
else:
    print("No menstrual cycle data entered.")
