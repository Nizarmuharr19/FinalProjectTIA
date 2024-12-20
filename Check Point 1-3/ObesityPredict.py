import pandas as pd
import numpy as np
import joblib

# Load Saved Model and Preprocessor
gb_model = joblib.load('gradient_boosting_model.pkl')
scaler = joblib.load('scaler.pkl')
label_encoders = joblib.load('label_encoders.pkl') 

# Input new data
new_data = {
    'Gender': 'Male',
    'Age': 25,
    'Height': 175,
    'Weight': 70,
    'family_history_with_overweight': 'yes',
    'FAVC': 'yes',
    'FCVC': 2,
    'NCP': 3,
    'CAEC': 'Sometimes',
    'SMOKE': 'no',
    'CH2O': 2,
    'SCC': 'no',
    'FAF': 1,
    'TUE': 2,
    'CALC': 'Sometimes',
    'MTRANS': 'Public_Transportation'
}

# Convert new data to DataFrame
new_data_df = pd.DataFrame([new_data])

# Encoding categorical
categorical_columns = ['Gender', 'family_history_with_overweight', 'FAVC', 
                       'CAEC', 'SMOKE', 'SCC', 'CALC', 'MTRANS']

for col in categorical_columns:
    if col in new_data_df.columns:
        new_data_df[col] = label_encoders[col].transform(new_data_df[col])

# Scalling numerical kolom
new_data_scaled = scaler.transform(new_data_df)

# Prediksi
prediction = gb_model.predict(new_data_scaled)
predicted_class = prediction[0]  # Prediksi Class

# Decode target label yang ter-encode
decoded_prediction = label_encoders['NObeyesdad'].inverse_transform([predicted_class])

print(f"Predicted Class (Encoded): {predicted_class}")
print(f"Predicted Class (Decoded): {decoded_prediction[0]}")
