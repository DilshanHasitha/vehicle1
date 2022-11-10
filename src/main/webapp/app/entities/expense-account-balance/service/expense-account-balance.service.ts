import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpenseAccountBalance, NewExpenseAccountBalance } from '../expense-account-balance.model';

export type PartialUpdateExpenseAccountBalance = Partial<IExpenseAccountBalance> & Pick<IExpenseAccountBalance, 'id'>;

export type EntityResponseType = HttpResponse<IExpenseAccountBalance>;
export type EntityArrayResponseType = HttpResponse<IExpenseAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseAccountBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-account-balances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expenseAccountBalance: NewExpenseAccountBalance): Observable<EntityResponseType> {
    return this.http.post<IExpenseAccountBalance>(this.resourceUrl, expenseAccountBalance, { observe: 'response' });
  }

  update(expenseAccountBalance: IExpenseAccountBalance): Observable<EntityResponseType> {
    return this.http.put<IExpenseAccountBalance>(
      `${this.resourceUrl}/${this.getExpenseAccountBalanceIdentifier(expenseAccountBalance)}`,
      expenseAccountBalance,
      { observe: 'response' }
    );
  }

  partialUpdate(expenseAccountBalance: PartialUpdateExpenseAccountBalance): Observable<EntityResponseType> {
    return this.http.patch<IExpenseAccountBalance>(
      `${this.resourceUrl}/${this.getExpenseAccountBalanceIdentifier(expenseAccountBalance)}`,
      expenseAccountBalance,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExpenseAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExpenseAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExpenseAccountBalanceIdentifier(expenseAccountBalance: Pick<IExpenseAccountBalance, 'id'>): number {
    return expenseAccountBalance.id;
  }

  compareExpenseAccountBalance(o1: Pick<IExpenseAccountBalance, 'id'> | null, o2: Pick<IExpenseAccountBalance, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseAccountBalanceIdentifier(o1) === this.getExpenseAccountBalanceIdentifier(o2) : o1 === o2;
  }

  addExpenseAccountBalanceToCollectionIfMissing<Type extends Pick<IExpenseAccountBalance, 'id'>>(
    expenseAccountBalanceCollection: Type[],
    ...expenseAccountBalancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseAccountBalances: Type[] = expenseAccountBalancesToCheck.filter(isPresent);
    if (expenseAccountBalances.length > 0) {
      const expenseAccountBalanceCollectionIdentifiers = expenseAccountBalanceCollection.map(
        expenseAccountBalanceItem => this.getExpenseAccountBalanceIdentifier(expenseAccountBalanceItem)!
      );
      const expenseAccountBalancesToAdd = expenseAccountBalances.filter(expenseAccountBalanceItem => {
        const expenseAccountBalanceIdentifier = this.getExpenseAccountBalanceIdentifier(expenseAccountBalanceItem);
        if (expenseAccountBalanceCollectionIdentifiers.includes(expenseAccountBalanceIdentifier)) {
          return false;
        }
        expenseAccountBalanceCollectionIdentifiers.push(expenseAccountBalanceIdentifier);
        return true;
      });
      return [...expenseAccountBalancesToAdd, ...expenseAccountBalanceCollection];
    }
    return expenseAccountBalanceCollection;
  }
}
