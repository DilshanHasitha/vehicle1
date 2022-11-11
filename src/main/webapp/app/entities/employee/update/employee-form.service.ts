import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id' | 'isActive' | 'vehicles'>;

type EmployeeFormGroupContent = {
  id: FormControl<IEmployee['id'] | NewEmployee['id']>;
  code: FormControl<IEmployee['code']>;
  firstName: FormControl<IEmployee['firstName']>;
  lastName: FormControl<IEmployee['lastName']>;
  email: FormControl<IEmployee['email']>;
  isActive: FormControl<IEmployee['isActive']>;
  phone: FormControl<IEmployee['phone']>;
  addressLine1: FormControl<IEmployee['addressLine1']>;
  addressLine2: FormControl<IEmployee['addressLine2']>;
  city: FormControl<IEmployee['city']>;
  country: FormControl<IEmployee['country']>;
  salary: FormControl<IEmployee['salary']>;
  user: FormControl<IEmployee['user']>;
  type: FormControl<IEmployee['type']>;
  vehicles: FormControl<IEmployee['vehicles']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = {
      ...this.getFormDefaults(),
      ...employee,
    };
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(employeeRawValue.code),
      firstName: new FormControl(employeeRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(employeeRawValue.lastName),
      email: new FormControl(employeeRawValue.email),
      isActive: new FormControl(employeeRawValue.isActive),
      phone: new FormControl(employeeRawValue.phone, {
        validators: [Validators.required],
      }),
      addressLine1: new FormControl(employeeRawValue.addressLine1),
      addressLine2: new FormControl(employeeRawValue.addressLine2),
      city: new FormControl(employeeRawValue.city),
      country: new FormControl(employeeRawValue.country),
      salary: new FormControl(employeeRawValue.salary),
      user: new FormControl(employeeRawValue.user),
      type: new FormControl(employeeRawValue.type),
      vehicles: new FormControl(employeeRawValue.vehicles ?? []),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return form.getRawValue() as IEmployee | NewEmployee;
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = { ...this.getFormDefaults(), ...employee };
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    return {
      id: null,
      isActive: false,
      vehicles: [],
    };
  }
}
