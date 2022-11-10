import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpense, NewExpense } from '../expense.model';

export type PartialUpdateExpense = Partial<IExpense> & Pick<IExpense, 'id'>;

export type EntityResponseType = HttpResponse<IExpense>;
export type EntityArrayResponseType = HttpResponse<IExpense[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expenses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expense: NewExpense): Observable<EntityResponseType> {
    return this.http.post<IExpense>(this.resourceUrl, expense, { observe: 'response' });
  }

  update(expense: IExpense): Observable<EntityResponseType> {
    return this.http.put<IExpense>(`${this.resourceUrl}/${this.getExpenseIdentifier(expense)}`, expense, { observe: 'response' });
  }

  partialUpdate(expense: PartialUpdateExpense): Observable<EntityResponseType> {
    return this.http.patch<IExpense>(`${this.resourceUrl}/${this.getExpenseIdentifier(expense)}`, expense, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExpense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpense[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExpenseIdentifier(expense: Pick<IExpense, 'id'>): number {
    return expense.id;
  }

  compareExpense(o1: Pick<IExpense, 'id'> | null, o2: Pick<IExpense, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseIdentifier(o1) === this.getExpenseIdentifier(o2) : o1 === o2;
  }

  addExpenseToCollectionIfMissing<Type extends Pick<IExpense, 'id'>>(
    expenseCollection: Type[],
    ...expensesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenses: Type[] = expensesToCheck.filter(isPresent);
    if (expenses.length > 0) {
      const expenseCollectionIdentifiers = expenseCollection.map(expenseItem => this.getExpenseIdentifier(expenseItem)!);
      const expensesToAdd = expenses.filter(expenseItem => {
        const expenseIdentifier = this.getExpenseIdentifier(expenseItem);
        if (expenseCollectionIdentifiers.includes(expenseIdentifier)) {
          return false;
        }
        expenseCollectionIdentifiers.push(expenseIdentifier);
        return true;
      });
      return [...expensesToAdd, ...expenseCollection];
    }
    return expenseCollection;
  }
}
