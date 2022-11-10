import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICashBookBalance, NewCashBookBalance } from '../cash-book-balance.model';

export type PartialUpdateCashBookBalance = Partial<ICashBookBalance> & Pick<ICashBookBalance, 'id'>;

export type EntityResponseType = HttpResponse<ICashBookBalance>;
export type EntityArrayResponseType = HttpResponse<ICashBookBalance[]>;

@Injectable({ providedIn: 'root' })
export class CashBookBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cash-book-balances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cashBookBalance: NewCashBookBalance): Observable<EntityResponseType> {
    return this.http.post<ICashBookBalance>(this.resourceUrl, cashBookBalance, { observe: 'response' });
  }

  update(cashBookBalance: ICashBookBalance): Observable<EntityResponseType> {
    return this.http.put<ICashBookBalance>(`${this.resourceUrl}/${this.getCashBookBalanceIdentifier(cashBookBalance)}`, cashBookBalance, {
      observe: 'response',
    });
  }

  partialUpdate(cashBookBalance: PartialUpdateCashBookBalance): Observable<EntityResponseType> {
    return this.http.patch<ICashBookBalance>(`${this.resourceUrl}/${this.getCashBookBalanceIdentifier(cashBookBalance)}`, cashBookBalance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICashBookBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICashBookBalance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCashBookBalanceIdentifier(cashBookBalance: Pick<ICashBookBalance, 'id'>): number {
    return cashBookBalance.id;
  }

  compareCashBookBalance(o1: Pick<ICashBookBalance, 'id'> | null, o2: Pick<ICashBookBalance, 'id'> | null): boolean {
    return o1 && o2 ? this.getCashBookBalanceIdentifier(o1) === this.getCashBookBalanceIdentifier(o2) : o1 === o2;
  }

  addCashBookBalanceToCollectionIfMissing<Type extends Pick<ICashBookBalance, 'id'>>(
    cashBookBalanceCollection: Type[],
    ...cashBookBalancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cashBookBalances: Type[] = cashBookBalancesToCheck.filter(isPresent);
    if (cashBookBalances.length > 0) {
      const cashBookBalanceCollectionIdentifiers = cashBookBalanceCollection.map(
        cashBookBalanceItem => this.getCashBookBalanceIdentifier(cashBookBalanceItem)!
      );
      const cashBookBalancesToAdd = cashBookBalances.filter(cashBookBalanceItem => {
        const cashBookBalanceIdentifier = this.getCashBookBalanceIdentifier(cashBookBalanceItem);
        if (cashBookBalanceCollectionIdentifiers.includes(cashBookBalanceIdentifier)) {
          return false;
        }
        cashBookBalanceCollectionIdentifiers.push(cashBookBalanceIdentifier);
        return true;
      });
      return [...cashBookBalancesToAdd, ...cashBookBalanceCollection];
    }
    return cashBookBalanceCollection;
  }
}
