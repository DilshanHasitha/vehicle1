import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeType, NewEmployeeType } from '../employee-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeType for edit and NewEmployeeTypeFormGroupInput for create.
 */
type EmployeeTypeFormGroupInput = IEmployeeType | PartialWithRequiredKeyOf<NewEmployeeType>;

type EmployeeTypeFormDefaults = Pick<NewEmployeeType, 'id' | 'isActive'>;

type EmployeeTypeFormGroupContent = {
  id: FormControl<IEmployeeType['id'] | NewEmployeeType['id']>;
  code: FormControl<IEmployeeType['code']>;
  name: FormControl<IEmployeeType['name']>;
  isActive: FormControl<IEmployeeType['isActive']>;
};

export type EmployeeTypeFormGroup = FormGroup<EmployeeTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeTypeFormService {
  createEmployeeTypeFormGroup(employeeType: EmployeeTypeFormGroupInput = { id: null }): EmployeeTypeFormGroup {
    const employeeTypeRawValue = {
      ...this.getFormDefaults(),
      ...employeeType,
    };
    return new FormGroup<EmployeeTypeFormGroupContent>({
      id: new FormControl(
        { value: employeeTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(employeeTypeRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(employeeTypeRawValue.name, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(employeeTypeRawValue.isActive),
    });
  }

  getEmployeeType(form: EmployeeTypeFormGroup): IEmployeeType | NewEmployeeType {
    return form.getRawValue() as IEmployeeType | NewEmployeeType;
  }

  resetForm(form: EmployeeTypeFormGroup, employeeType: EmployeeTypeFormGroupInput): void {
    const employeeTypeRawValue = { ...this.getFormDefaults(), ...employeeType };
    form.reset(
      {
        ...employeeTypeRawValue,
        id: { value: employeeTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeTypeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
