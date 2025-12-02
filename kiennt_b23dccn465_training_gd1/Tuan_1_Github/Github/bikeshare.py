import time
import pandas as pd
import numpy as np

CITY_DATA = { 'Chicago': 'chicago.csv',
              'New York': 'new_york_city.csv',
              'Washington': 'washington.csv' }
# define danh sach thang
months = ['January', 'February', 'March', 'April', 'May', 'June']
days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']

# hello, coi nhu e da sua code nhe!

def get_filters():
    """
    Asks user to specify a city, month, and day to analyze.

    Returns:
        (str) city - name of the city to analyze
        (str) month - name of the month to filter by, or "all" to apply no month filter
        (str) day - name of the day of week to filter by, or "all" to apply no day filter
    """
    print('Hello! Let\'s explore some US bikeshare data!')

    # get user input for city (chicago, new york city, washington). HINT: Use a while loop to handle invalid inputs
    try:
        while True:
            city = input('\nWould you like to see data for Chicago, New York, or Washington?\n')
            if (city.title() != 'Chicago' and city.title() != 'New York' and city.title() != 'Washington'):
                print('\nInvalid city. Please enter "Chicago" or "New York" or "Washington"!\n')
            else:
                break
    except KeyboardInterrupt:
        print('\nNo input taken\n')
        exit()

    print('\nLook like you want to hear about {}!'.format(city.title()))

    # get user input for month (all, january, february, ... , june)
    try:
        check_loop = True
        messsage = ''
        while check_loop:
            month = input('\nWhich month you like to filter the data? January, February, March, April, May, or June?\n')
            for i in months:
                if month.title() == i:
                    check_loop = False
                    messsage = 'Valid month'
                    break
            if messsage == 'Valid month':
                continue
            messsage = '\nInvalid month. Please enter day in ("January", "February", "March", "April", ' \
                       '"May", ' \
                       '"June")!\n '
            print(messsage)
    except KeyboardInterrupt:
        print('\nNo input taken\n')
        exit()

    # get user input for day of week (all, monday, tuesday, ... sunday)
    try:
        check_loop = True
        messsage = ''
        while check_loop:
            day = input('\nWhich day you like to filter the data? Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday\n')
            for i in days:
                if day.title() == i:
                    check_loop = False
                    messsage = 'Valid day'
                    break
            if messsage != 'Valid day':
                messsage = '\nInvalid day. Please enter day in ("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")!\n'
                print(messsage)
    except KeyboardInterrupt:
        print('\nNo input taken\n')
        exit()

    print('-'*40)
    return city, month, day


def load_data(city, month, day):
    """
    Loads data for the specified city and filters by month and day if applicable.

    Args:
        (str) city - name of the city to analyze
        (str) month - name of the month to filter by, or "all" to apply no month filter
        (str) day - name of the day of week to filter by, or "all" to apply no day filter
    Returns:
        df - Pandas DataFrame containing city data filtered by month and day
    """

    # load data file into a dataframe
    df = pd.read_csv(CITY_DATA[city.title()])

    # convert the Start Time column to datetime
    df['Start Time'] = pd.to_datetime(df['Start Time'])

    # extract month and day of week from Start Time to create new columns
    df['month'] = df['Start Time'].dt.month
    df['day_of_week'] = df['Start Time'].dt.day_name()

    # filter by month if applicable
    if month.title() != 'all':
        # use the index of the months list to get the corresponding int
        month = months.index(month.title()) + 1

        # filter by month to create the new dataframe
        df = df[df['month'] == month]

    # filter by day of week if applicable
    if day.title() != 'all':
        # filter by day of week to create the new dataframe
        df = df[df['day_of_week'] == day.title()]

    return df


def time_stats(df):
    """Displays statistics on the most frequent times of travel."""

    print('\nCalculating The Most Frequent Times of Travel...\n')
    start_time = time.time()

    # display the most common month
    common_month = df['month'].value_counts().idxmax()
    print('The most common month: {}\n'.format(common_month))

    # display the most common day of week
    common_day_of_week = df['day_of_week'].value_counts().idxmax()
    print('The most common day of week: {}\n'.format(common_day_of_week))

    # display the most common start hour
    common_start_hour = df['Start Time'].dt.hour.value_counts().idxmax()
    print('The most common start hour: {}\n'.format(common_start_hour))

    print("\nThis took %s seconds." % (time.time() - start_time))
    print('-'*40)


def station_stats(df):
    """Displays statistics on the most popular stations and trip."""

    print('\nCalculating The Most Popular Stations and Trip...\n')
    start_time = time.time()

    # display most commonly used start station
    common_start_station = df['Start Station'].value_counts().idxmax()
    print('The most commonly used start station: {}\n'.format(common_start_station))

    # display most commonly used end station
    common_end_station  = df['End Station'].value_counts().idxmax()
    print('The most commonly used end station: {}\n'.format(common_end_station))

    # display most frequent combination of start station and end station trip
    print('The most frequent start station and end station trip: Start Station - {}; End Station - {}\n'.format(common_start_station, common_end_station))

    print("\nThis took %s seconds." % (time.time() - start_time))
    print('-'*40)


def trip_duration_stats(df):
    """Displays statistics on the total and average trip duration."""

    print('\nCalculating Trip Duration...\n')
    start_time = time.time()

    # display total travel time
    total_travel_time = df['Trip Duration'].sum()
    print('Total travel time: {}\n'.format(total_travel_time))

    # display mean travel time
    mean_travel_time = df['Trip Duration'].mean()
    print('Mean travel time: {}\n'.format(round(mean_travel_time, 2)))

    print("\nThis took %s seconds." % (time.time() - start_time))
    print('-'*40)


def user_stats(df):
    """Displays statistics on bikeshare users."""

    print('\nCalculating User Stats...\n')
    start_time = time.time()

    # Display counts of user types
    user_types = df['User Type'].value_counts()
    print('Counts of user types:\n{}'.format(user_types))

    # Display counts of gender
    if {'Gender', 'Birth Year'}.issubset(df.columns):
        gender = df['Gender'].value_counts()
        print('\nCounts of gender:\n{}'.format(gender))

    # Display earliest, most recent, and most common year of birth

        birth_year_earliest = df['Birth Year'].min()
        birth_year_recent = df['Birth Year'].max()
        birth_year_common = df['Birth Year'].value_counts().idxmax()

        print('\nThe earliest year of birth: {}\n'.format(int(birth_year_earliest)))
        print('The most recent year of birth: {}\n'.format(int(birth_year_recent)))
        print('The most common year of birth: {}\n'.format(int(birth_year_common)))

    print("\nThis took %s seconds." % (time.time() - start_time))
    print('-'*40)


def view_raw_data(df):
    """
    Display raw data if you want to see.
    """
    row = 0
    while True:
        raw_data = input("\nWould you like to view individual trip data? Type 'yes' or 'no'.\n")
        if raw_data.lower() == 'yes':
            print(df.iloc[row:row+5])
            row += 5
        elif raw_data.lower() == 'no':
            break

def main():
    while True:
        city, month, day = get_filters()
        df = load_data(city, month, day)
        df2 = pd.read_csv(CITY_DATA[city.title()])

        time_stats(df)
        station_stats(df)
        trip_duration_stats(df)
        user_stats(df)
        view_raw_data(df2)

        restart = input('\nWould you like to restart? Enter yes or no.\n')
        if restart.lower() != 'yes':
            break


if __name__ == "__main__":
	main()


#in refractor branch 
