import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExpense, NewExpense } from '../expense.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpense for edit and NewExpenseFormGroupInput for create.
 */
type ExpenseFormGroupInput = IExpense | PartialWithRequiredKeyOf<NewExpense>;

type ExpenseFormDefaults = Pick<NewExpense, 'id' | 'isActive'>;

type ExpenseFormGroupContent = {
  id: FormControl<IExpense['id'] | NewExpense['id']>;
  expenseCode: FormControl<IExpense['expenseCode']>;
  expenseName: FormControl<IExpense['expenseName']>;
  expenseLimit: FormControl<IExpense['expenseLimit']>;
  isActive: FormControl<IExpense['isActive']>;
};

export type ExpenseFormGroup = FormGroup<ExpenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseFormService {
  createExpenseFormGroup(expense: ExpenseFormGroupInput = { id: null }): ExpenseFormGroup {
    const expenseRawValue = {
      ...this.getFormDefaults(),
      ...expense,
    };
    return new FormGroup<ExpenseFormGroupContent>({
      id: new FormControl(
        { value: expenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      expenseCode: new FormControl(expenseRawValue.expenseCode, {
        validators: [Validators.required],
      }),
      expenseName: new FormControl(expenseRawValue.expenseName, {
        validators: [Validators.required],
      }),
      expenseLimit: new FormControl(expenseRawValue.expenseLimit),
      isActive: new FormControl(expenseRawValue.isActive),
    });
  }

  getExpense(form: ExpenseFormGroup): IExpense | NewExpense {
    return form.getRawValue() as IExpense | NewExpense;
  }

  resetForm(form: ExpenseFormGroup, expense: ExpenseFormGroupInput): void {
    const expenseRawValue = { ...this.getFormDefaults(), ...expense };
    form.reset(
      {
        ...expenseRawValue,
        id: { value: expenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ExpenseFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
