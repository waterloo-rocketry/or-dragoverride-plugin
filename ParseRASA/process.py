import csv
import os
import pandas as pd

def simple_process_data(input_txt, output_csv):
    with open(input_txt, "r") as f, open(output_csv, "w", newline="") as csvfile:
        lines = [
            line.strip() for line in f if line.strip() and line.strip()[0].isdigit()
        ]
        csvwriter = csv.writer(csvfile)
        csvwriter.writerow(["Mach", "Alpha", "CD_Off", "CD_On"])
        for i in range(0, len(lines), 6):
            five_lines = lines[i : i + 6]
            processed_lines = [list(map(float, line.split())) for line in five_lines]
            if not all(line[0] == processed_lines[0][0] for line in processed_lines):
                raise ValueError("The first column of the five lines is not the same")

            final_line = [
                processed_lines[0][0],
                processed_lines[2][1],
                processed_lines[0][2],
                processed_lines[0][3],
            ]  # Mach, Alpha degree, CD Power Off, CD Power On

            csvwriter.writerow(final_line)

def read_csv_files(csv_list, output_csv):
    merged_df = pd.concat((pd.read_csv(file) for file in csv_list), ignore_index=True)
    merged_df.to_csv(output_csv, index=False)

input_txt = [f"Cycle0_6inNozzle_AoA{i}.txt" for i in range(11)]
output_csv = [f"out/Cycle0_6inNozzle_AoA{i}.csv" for i in range(11)]

if not os.path.exists("out"):
    os.makedirs("out")

for i in range(len(input_txt)):
    simple_process_data(input_txt[i], output_csv[i])

read_csv_files(output_csv, "Cycle0_6inNozzle.csv")
