import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExpenseType, NewExpenseType } from '../expense-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseType for edit and NewExpenseTypeFormGroupInput for create.
 */
type ExpenseTypeFormGroupInput = IExpenseType | PartialWithRequiredKeyOf<NewExpenseType>;

type ExpenseTypeFormDefaults = Pick<NewExpenseType, 'id' | 'isActive'>;

type ExpenseTypeFormGroupContent = {
  id: FormControl<IExpenseType['id'] | NewExpenseType['id']>;
  code: FormControl<IExpenseType['code']>;
  name: FormControl<IExpenseType['name']>;
  isActive: FormControl<IExpenseType['isActive']>;
};

export type ExpenseTypeFormGroup = FormGroup<ExpenseTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseTypeFormService {
  createExpenseTypeFormGroup(expenseType: ExpenseTypeFormGroupInput = { id: null }): ExpenseTypeFormGroup {
    const expenseTypeRawValue = {
      ...this.getFormDefaults(),
      ...expenseType,
    };
    return new FormGroup<ExpenseTypeFormGroupContent>({
      id: new FormControl(
        { value: expenseTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(expenseTypeRawValue.code, {
        validators: [Validators.required],
      }),
      name: new FormControl(expenseTypeRawValue.name, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(expenseTypeRawValue.isActive),
    });
  }

  getExpenseType(form: ExpenseTypeFormGroup): IExpenseType | NewExpenseType {
    return form.getRawValue() as IExpenseType | NewExpenseType;
  }

  resetForm(form: ExpenseTypeFormGroup, expenseType: ExpenseTypeFormGroupInput): void {
    const expenseTypeRawValue = { ...this.getFormDefaults(), ...expenseType };
    form.reset(
      {
        ...expenseTypeRawValue,
        id: { value: expenseTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExpenseTypeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
