import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVehicle, NewVehicle } from '../vehicle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVehicle for edit and NewVehicleFormGroupInput for create.
 */
type VehicleFormGroupInput = IVehicle | PartialWithRequiredKeyOf<NewVehicle>;

type VehicleFormDefaults = Pick<NewVehicle, 'id' | 'isActive'>;

type VehicleFormGroupContent = {
  id: FormControl<IVehicle['id'] | NewVehicle['id']>;
  code: FormControl<IVehicle['code']>;
  name: FormControl<IVehicle['name']>;
  relatedUserLogin: FormControl<IVehicle['relatedUserLogin']>;
  expenceCode: FormControl<IVehicle['expenceCode']>;
  isActive: FormControl<IVehicle['isActive']>;
  merchant: FormControl<IVehicle['merchant']>;
};

export type VehicleFormGroup = FormGroup<VehicleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VehicleFormService {
  createVehicleFormGroup(vehicle: VehicleFormGroupInput = { id: null }): VehicleFormGroup {
    const vehicleRawValue = {
      ...this.getFormDefaults(),
      ...vehicle,
    };
    return new FormGroup<VehicleFormGroupContent>({
      id: new FormControl(
        { value: vehicleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(vehicleRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(vehicleRawValue.name, {
        validators: [Validators.required],
      }),
      relatedUserLogin: new FormControl(vehicleRawValue.relatedUserLogin),
      expenceCode: new FormControl(vehicleRawValue.expenceCode),
      isActive: new FormControl(vehicleRawValue.isActive),
      merchant: new FormControl(vehicleRawValue.merchant),
    });
  }

  getVehicle(form: VehicleFormGroup): IVehicle | NewVehicle {
    return form.getRawValue() as IVehicle | NewVehicle;
  }

  resetForm(form: VehicleFormGroup, vehicle: VehicleFormGroupInput): void {
    const vehicleRawValue = { ...this.getFormDefaults(), ...vehicle };
    form.reset(
      {
        ...vehicleRawValue,
        id: { value: vehicleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VehicleFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
