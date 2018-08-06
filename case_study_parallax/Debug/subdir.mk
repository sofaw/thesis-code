################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../case_study_parallax.c 

OBJS += \
./case_study_parallax.o 

C_DEPS += \
./case_study_parallax.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.c
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	gcc -I"/Users/sophie/SimpleIDE/Learn/Simple Libraries/Utility/libsimpletools" -I"/Users/sophie/SimpleIDE/Learn/Simple Libraries/Sensor/libdht22" -I"/Users/sophie/SimpleIDE/Learn/Simple Libraries/Robotics/ActivityBot/libabdrive" -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


