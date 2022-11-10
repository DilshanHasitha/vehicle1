import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenseAccount } from '../expense-account.model';
import { ExpenseAccountService } from '../service/expense-account.service';

@Injectable({ providedIn: 'root' })
export class ExpenseAccountRoutingResolveService implements Resolve<IExpenseAccount | null> {
  constructor(protected service: ExpenseAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpenseAccount | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expenseAccount: HttpResponse<IExpenseAccount>) => {
          if (expenseAccount.body) {
            return of(expenseAccount.body);
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
