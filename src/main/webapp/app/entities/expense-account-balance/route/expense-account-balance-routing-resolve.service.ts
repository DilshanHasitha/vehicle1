import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenseAccountBalance } from '../expense-account-balance.model';
import { ExpenseAccountBalanceService } from '../service/expense-account-balance.service';

@Injectable({ providedIn: 'root' })
export class ExpenseAccountBalanceRoutingResolveService implements Resolve<IExpenseAccountBalance | null> {
  constructor(protected service: ExpenseAccountBalanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseAccountBalance | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expenseAccountBalance: HttpResponse<IExpenseAccountBalance>) => {
          if (expenseAccountBalance.body) {
            return of(expenseAccountBalance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
